/*    */ package com.ibm.icu.impl.locale;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LocaleObjectCache<K, V>
/*    */ {
/*    */   private ConcurrentHashMap<K, CacheEntry<K, V>> _map;
/* 15 */   private ReferenceQueue<V> _queue = new ReferenceQueue();
/*    */   
/*    */   public LocaleObjectCache() {
/* 18 */     this(16, 0.75F, 16);
/*    */   }
/*    */   
/*    */   public LocaleObjectCache(int initialCapacity, float loadFactor, int concurrencyLevel) {
/* 22 */     this._map = new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
/*    */   }
/*    */   
/*    */   public V get(K key) {
/* 26 */     V value = null;
/*    */     
/* 28 */     cleanStaleEntries();
/* 29 */     CacheEntry<K, V> entry = (CacheEntry)this._map.get(key);
/* 30 */     if (entry != null) {
/* 31 */       value = entry.get();
/*    */     }
/* 33 */     if (value == null) {
/* 34 */       key = normalizeKey(key);
/* 35 */       V newVal = createObject(key);
/* 36 */       if ((key == null) || (newVal == null))
/*    */       {
/* 38 */         return null;
/*    */       }
/*    */       
/* 41 */       CacheEntry<K, V> newEntry = new CacheEntry(key, newVal, this._queue);
/*    */       
/* 43 */       while (value == null) {
/* 44 */         cleanStaleEntries();
/* 45 */         entry = (CacheEntry)this._map.putIfAbsent(key, newEntry);
/* 46 */         if (entry == null) {
/* 47 */           value = newVal;
/* 48 */           break;
/*    */         }
/* 50 */         value = entry.get();
/*    */       }
/*    */     }
/*    */     
/* 54 */     return value;
/*    */   }
/*    */   
/*    */   private void cleanStaleEntries()
/*    */   {
/*    */     CacheEntry<K, V> entry;
/* 60 */     while ((entry = (CacheEntry)this._queue.poll()) != null) {
/* 61 */       this._map.remove(entry.getKey());
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract V createObject(K paramK);
/*    */   
/*    */   protected K normalizeKey(K key) {
/* 68 */     return key;
/*    */   }
/*    */   
/*    */   private static class CacheEntry<K, V> extends SoftReference<V> {
/*    */     private K _key;
/*    */     
/*    */     CacheEntry(K key, V value, ReferenceQueue<V> queue) {
/* 75 */       super(queue);
/* 76 */       this._key = key;
/*    */     }
/*    */     
/*    */     K getKey() {
/* 80 */       return (K)this._key;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Ethan\Desktop\FontZip\FontTool\sfnttool.jar!\com\ibm\icu\impl\locale\LocaleObjectCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */