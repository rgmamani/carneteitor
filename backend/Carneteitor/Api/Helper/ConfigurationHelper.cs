using System.Web.Configuration;

namespace Api.Helper
{
    public class ConfigurationHelper
    {
        public static string DataFile = WebConfigurationManager.AppSettings["ArchivoDeDatos"];
        public static string DatabaseFile = WebConfigurationManager.AppSettings["BaseDeDatos"];
        public static string SheetName = WebConfigurationManager.AppSettings["NombreHoja"];
        public static string Id = WebConfigurationManager.AppSettings["NumeroAfiliado"];
        public static string DocumentColumn = WebConfigurationManager.AppSettings["Documento"];
        public static string NameColumn = WebConfigurationManager.AppSettings["Nombre"];
        public static string LastnameColumn = WebConfigurationManager.AppSettings["Apellido"];
        public static string StateColumn = WebConfigurationManager.AppSettings["Localidad"];
        public static string ProvinceColumn = WebConfigurationManager.AppSettings["Provincia"];
    }
}