/*     */ package com.ibm.icu.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EventListener;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ICUNotifier
/*     */ {
/*     */   private final Object notifyLock;
/*     */   private NotifyThread notifyThread;
/*     */   private List<EventListener> listeners;
/*     */   
/*     */   public ICUNotifier()
/*     */   {
/*  31 */     this.notifyLock = new Object();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addListener(EventListener l)
/*     */   {
/*  43 */     if (l == null) {
/*  44 */       throw new NullPointerException();
/*     */     }
/*     */     
/*  47 */     if (acceptsListener(l)) {
/*  48 */       synchronized (this.notifyLock) {
/*  49 */         if (this.listeners == null) {
/*  50 */           this.listeners = new ArrayList();
/*     */         }
/*     */         else {
/*  53 */           for (EventListener ll : this.listeners) {
/*  54 */             if (ll == l) {
/*  55 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*  60 */         this.listeners.add(l);
/*     */       }
/*     */     } else {
/*  63 */       throw new IllegalStateException("Listener invalid for this notifier.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeListener(EventListener l)
/*     */   {
/*  73 */     if (l == null) {
/*  74 */       throw new NullPointerException();
/*     */     }
/*  76 */     synchronized (this.notifyLock) {
/*  77 */       if (this.listeners != null)
/*     */       {
/*  79 */         Iterator<EventListener> iter = this.listeners.iterator();
/*  80 */         while (iter.hasNext()) {
/*  81 */           if (iter.next() == l) {
/*  82 */             iter.remove();
/*  83 */             if (this.listeners.size() == 0) {
/*  84 */               this.listeners = null;
/*     */             }
/*  86 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyChanged()
/*     */   {
/*  99 */     if (this.listeners != null)
/* 100 */       synchronized (this.notifyLock) {
/* 101 */         if (this.listeners != null) {
/* 102 */           if (this.notifyThread == null) {
/* 103 */             this.notifyThread = new NotifyThread(this);
/* 104 */             this.notifyThread.setDaemon(true);
/* 105 */             this.notifyThread.start();
/*     */           }
/* 107 */           this.notifyThread.queue((EventListener[])this.listeners.toArray(new EventListener[this.listeners.size()]));
/*     */         }
/*     */       }
/*     */   }
/*     */   
/*     */   protected abstract boolean acceptsListener(EventListener paramEventListener);
/*     */   
/*     */   protected abstract void notifyListener(EventListener paramEventListener);
/*     */   
/*     */   private static class NotifyThread extends Thread {
/*     */     private final ICUNotifier notifier;
/* 118 */     private final List<EventListener[]> queue = new ArrayList();
/*     */     
/*     */     NotifyThread(ICUNotifier notifier) {
/* 121 */       this.notifier = notifier;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void queue(EventListener[] list)
/*     */     {
/* 128 */       synchronized (this) {
/* 129 */         this.queue.add(list);
/* 130 */         notify();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         for (;;)
/*     */         {
/*     */           EventListener[] list;
/* 142 */           synchronized (this) {
/* 143 */             if (this.queue.isEmpty()) {
/* 144 */               wait(); continue;
/*     */             }
/* 146 */             list = (EventListener[])this.queue.remove(0);
/*     */           }
/*     */           
/* 149 */           for (int i = 0; i < list.length; i++) {
/* 150 */             this.notifier.notifyListener(list[i]);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\ICUNotifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */