/*    */ package com.ibm.icu.impl;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleCache<K, V>
/*    */   implements ICUCache<K, V>
/*    */ {
/*    */   private static final int DEFAULT_CAPACITY = 16;
/* 20 */   private Reference<Map<K, V>> cacheRef = null;
/* 21 */   private int type = 0;
/* 22 */   private int capacity = 16;
/*    */   
/*    */   public SimpleCache() {}
/*    */   
/*    */   public SimpleCache(int cacheType)
/*    */   {
/* 28 */     this(cacheType, 16);
/*    */   }
/*    */   
/*    */   public SimpleCache(int cacheType, int initialCapacity) {
/* 32 */     if (cacheType == 1) {
/* 33 */       this.type = cacheType;
/*    */     }
/* 35 */     if (initialCapacity > 0) {
/* 36 */       this.capacity = initialCapacity;
/*    */     }
/*    */   }
/*    */   
/*    */   public V get(Object key) {
/* 41 */     Reference<Map<K, V>> ref = this.cacheRef;
/* 42 */     if (ref != null) {
/* 43 */       Map<K, V> map = (Map)ref.get();
/* 44 */       if (map != null) {
/* 45 */         return (V)map.get(key);
/*    */       }
/*    */     }
/* 48 */     return null;
/*    */   }
/*    */   
/*    */   public void put(K key, V value) {
/* 52 */     Reference<Map<K, V>> ref = this.cacheRef;
/* 53 */     Map<K, V> map = null;
/* 54 */     if (ref != null) {
/* 55 */       map = (Map)ref.get();
/*    */     }
/* 57 */     if (map == null) {
/* 58 */       map = Collections.synchronizedMap(new HashMap(this.capacity));
/* 59 */       if (this.type == 1) {
/* 60 */         ref = new WeakReference(map);
/*    */       } else {
/* 62 */         ref = new SoftReference(map);
/*    */       }
/* 64 */       this.cacheRef = ref;
/*    */     }
/* 66 */     map.put(key, value);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 70 */     this.cacheRef = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\SimpleCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */