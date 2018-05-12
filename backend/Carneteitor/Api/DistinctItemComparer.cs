using System.Collections.Generic;
using Api.Models;

namespace Api
{
    internal class DistinctItemComparer : IEqualityComparer<AfiliadoModel>
    {
        public bool Equals(AfiliadoModel x, AfiliadoModel y)
        {
            return x.Documento == y.Documento;
        }

        public int GetHashCode(AfiliadoModel obj)
        {
            if (string.IsNullOrEmpty(obj.Documento))
            {
                return 0;
            }

            return obj.Documento.GetHashCode();
        }
    }
}