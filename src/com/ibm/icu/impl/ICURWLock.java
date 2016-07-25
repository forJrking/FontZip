/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ICURWLock
/*     */ {
/*     */   private Object writeLock;
/*     */   
/*     */ 
/*     */   private Object readLock;
/*     */   
/*     */ 
/*     */   private int wwc;
/*     */   
/*     */ 
/*     */   private int rc;
/*     */   
/*     */ 
/*     */   private int wrc;
/*     */   
/*     */ 
/*     */   private Stats stats;
/*     */   
/*     */ 
/*     */   private static final int NOTIFY_NONE = 0;
/*     */   
/*     */   private static final int NOTIFY_WRITERS = 1;
/*     */   
/*     */   private static final int NOTIFY_READERS = 2;
/*     */   
/*     */ 
/*     */   public ICURWLock()
/*     */   {
/*  34 */     this.writeLock = new Object();
/*  35 */     this.readLock = new Object();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  40 */     this.stats = new Stats(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Stats
/*     */   {
/*     */     public int _rc;
/*     */     
/*     */ 
/*     */ 
/*     */     public int _mrc;
/*     */     
/*     */ 
/*     */ 
/*     */     public int _wrc;
/*     */     
/*     */ 
/*     */ 
/*     */     public int _wc;
/*     */     
/*     */ 
/*     */ 
/*     */     public int _wwc;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Stats() {}
/*     */     
/*     */ 
/*     */ 
/*     */     private Stats(int rc, int mrc, int wrc, int wc, int wwc)
/*     */     {
/*  75 */       this._rc = rc;
/*  76 */       this._mrc = mrc;
/*  77 */       this._wrc = wrc;
/*  78 */       this._wc = wc;
/*  79 */       this._wwc = wwc;
/*     */     }
/*     */     
/*     */     private Stats(Stats rhs) {
/*  83 */       this(rhs._rc, rhs._mrc, rhs._wrc, rhs._wc, rhs._wwc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/*  90 */       return " rc: " + this._rc + " mrc: " + this._mrc + " wrc: " + this._wrc + " wc: " + this._wc + " wwc: " + this._wwc;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Stats resetStats()
/*     */   {
/* 102 */     Stats result = this.stats;
/* 103 */     this.stats = new Stats(null);
/* 104 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized Stats clearStats()
/*     */   {
/* 111 */     Stats result = this.stats;
/* 112 */     this.stats = null;
/* 113 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized Stats getStats()
/*     */   {
/* 120 */     return this.stats == null ? null : new Stats(this.stats, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized boolean gotRead()
/*     */   {
/* 126 */     this.rc += 1;
/* 127 */     if (this.stats != null) {
/* 128 */       this.stats._rc += 1;
/* 129 */       if (this.rc > 1) this.stats._mrc += 1;
/*     */     }
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   private synchronized boolean getRead() {
/* 135 */     if ((this.rc >= 0) && (this.wwc == 0)) {
/* 136 */       return gotRead();
/*     */     }
/* 138 */     this.wrc += 1;
/* 139 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized boolean retryRead() {
/* 143 */     if (this.stats != null) this.stats._wrc += 1;
/* 144 */     if ((this.rc >= 0) && (this.wwc == 0)) {
/* 145 */       this.wrc -= 1;
/* 146 */       return gotRead();
/*     */     }
/* 148 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized boolean finishRead() {
/* 152 */     if (this.rc > 0) {
/* 153 */       return (0 == --this.rc) && (this.wwc > 0);
/*     */     }
/* 155 */     throw new IllegalStateException("no current reader to release");
/*     */   }
/*     */   
/*     */   private synchronized boolean gotWrite() {
/* 159 */     this.rc = -1;
/* 160 */     if (this.stats != null) {
/* 161 */       this.stats._wc += 1;
/*     */     }
/* 163 */     return true;
/*     */   }
/*     */   
/*     */   private synchronized boolean getWrite() {
/* 167 */     if (this.rc == 0) {
/* 168 */       return gotWrite();
/*     */     }
/* 170 */     this.wwc += 1;
/* 171 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized boolean retryWrite() {
/* 175 */     if (this.stats != null) this.stats._wwc += 1;
/* 176 */     if (this.rc == 0) {
/* 177 */       this.wwc -= 1;
/* 178 */       return gotWrite();
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized int finishWrite()
/*     */   {
/* 188 */     if (this.rc < 0) {
/* 189 */       this.rc = 0;
/* 190 */       if (this.wwc > 0)
/* 191 */         return 1;
/* 192 */       if (this.wrc > 0) {
/* 193 */         return 2;
/*     */       }
/* 195 */       return 0;
/*     */     }
/*     */     
/* 198 */     throw new IllegalStateException("no current writer to release");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acquireRead()
/*     */   {
/* 212 */     if (!getRead()) {
/*     */       try {
/*     */         for (;;) {
/* 215 */           synchronized (this.readLock) {
/* 216 */             this.readLock.wait();
/*     */           }
/* 218 */           if (retryRead()) {
/* 219 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseRead()
/*     */   {
/* 237 */     if (finishRead()) {
/* 238 */       synchronized (this.writeLock) {
/* 239 */         this.writeLock.notify();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acquireWrite()
/*     */   {
/* 256 */     if (!getWrite()) {
/*     */       try {
/*     */         for (;;) {
/* 259 */           synchronized (this.writeLock) {
/* 260 */             this.writeLock.wait();
/*     */           }
/* 262 */           if (retryWrite()) {
/* 263 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseWrite()
/*     */   {
/* 282 */     switch (finishWrite()) {
/*     */     case 1: 
/* 284 */       synchronized (this.writeLock) {
/* 285 */         this.writeLock.notify();
/*     */       }
/* 287 */       break;
/*     */     case 2: 
/* 289 */       synchronized (this.readLock) {
/* 290 */         this.readLock.notifyAll();
/*     */       }
/* 292 */       break;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICURWLock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */