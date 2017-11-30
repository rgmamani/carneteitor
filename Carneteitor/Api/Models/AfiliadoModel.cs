using LiteDB;

namespace Api.Models
{
    public class AfiliadoModel
    {
        [BsonId]
        public string Documento { get; set; }
        public string Nombre { get; set; }
        public string Apellido { get; set; }
        public string Localidad { get; set; }
        public string Provincia { get; set; }
    }
}