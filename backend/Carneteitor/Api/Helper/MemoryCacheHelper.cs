using System;
using System.Runtime.Caching;

namespace Api.Helper
{
    public static class MemoryCacheHelper
    {
        public static object GetValue(string key)
        {
            var memoryCache = MemoryCache.Default;
            return memoryCache.Get(key);
        }

        public static bool Add(string key, object value, DateTimeOffset absExpiration)
        {
            var memoryCache = MemoryCache.Default;
            return memoryCache.Add(key, value, absExpiration);
        }

        public static void Delete(string key)
        {
            var memoryCache = MemoryCache.Default;

            if (memoryCache.Contains(key))
            {
                memoryCache.Remove(key);
            }
        }
    }
}