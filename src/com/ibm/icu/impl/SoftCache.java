/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SoftCache<K, V, D>
/*    */   extends CacheBase<K, V, D>
/*    */ {
/*    */   public final V getInstance(K key, D data)
/*    */   {
/* 39 */     SettableSoftReference<V> valueRef = (SettableSoftReference)this.map.get(key);
/*    */     
/* 41 */     if (valueRef != null) {
/* 42 */       synchronized (valueRef) {
/* 43 */         value = valueRef.ref.get();
/* 44 */         if (value != null) {
/* 45 */           return value;
/*    */         }
/*    */         
/*    */ 
/* 49 */         valueRef.ref = new SoftReference(value = createInstance(key, data));
/* 50 */         return value;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 55 */     V value = createInstance(key, data);
/* 56 */     valueRef = (SettableSoftReference)this.map.putIfAbsent(key, new SettableSoftReference(value, null));
/* 57 */     if (valueRef == null)
/*    */     {
/* 59 */       return value;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 64 */     return (V)valueRef.setIfAbsent(value);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static final class SettableSoftReference<V>
/*    */   {
/*    */     private SoftReference<V> ref;
/*    */     
/*    */ 
/*    */ 
/*    */     private SettableSoftReference(V value)
/*    */     {
/* 77 */       this.ref = new SoftReference(value);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     private synchronized V setIfAbsent(V value)
/*    */     {
/* 87 */       V oldValue = this.ref.get();
/* 88 */       if (oldValue == null) {
/* 89 */         this.ref = new SoftReference(value);
/* 90 */         return value;
/*    */       }
/* 92 */       return oldValue;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/* 97 */   private ConcurrentHashMap<K, SettableSoftReference<V>> map = new ConcurrentHashMap();
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\SoftCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */