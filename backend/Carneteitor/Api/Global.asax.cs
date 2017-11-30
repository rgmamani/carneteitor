using Api.Helper;
using Api.Models;
using LiteDB;
using System.Web;
using System.Web.Http;

namespace Api
{
    public class WebApiApplication : HttpApplication
    {
        protected void Application_Start()
        {
            GlobalConfiguration.Configure(WebApiConfig.Register);
            LoadData(); 
        }

        private void LoadData()
        {
            var filePath = HttpContext.Current.Server.MapPath(string.Format("~/App_Data/{0}", ConfigurationHelper.DataFile));
            var list = ReadDataHelper.Read(filePath);
            var databasePath = HttpContext.Current.Server.MapPath(string.Format("~/App_Data/{0}", ConfigurationHelper.DatabaseFile));

            // Open database (or create if not exits)
            using (var db = new LiteDatabase(databasePath))
            {
                // Get customer collection
                var customers = db.GetCollection<AfiliadoModel>("customers");

                foreach (var item in list)
                {
                    // Insert new afiliado (Id will be auto-incremented)
                    customers.Insert(item);
                }

                // Index document using a document property
                customers.EnsureIndex(x => x.Documento);
            }
        }
    }
}