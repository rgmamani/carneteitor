using Api.Helper;
using Api.Models;
using LiteDB;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

[assembly: log4net.Config.XmlConfigurator(Watch = true)]
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
            var filePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/{0}", ConfigurationHelper.DataFile));
            var list = ReadDataHelper.Read(filePath);
            var sanityList = list.Where(x => !string.IsNullOrEmpty(x.Documento)).ToList();
            var distinctItems = sanityList.Distinct(new DistinctItemComparer()).ToList();
            var databasePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/{0}", ConfigurationHelper.DatabaseFile));

            // Open database (or create if not exits)
            using (var db = new LiteDatabase(databasePath))
            {
                // Get customer collection
                var customers = db.GetCollection<AfiliadoModel>("customers");

                if (customers.Count() > 0)
                {
                    db.DropCollection("customers");
                    customers.DropIndex("Documento");
                }

                // Inserting a high volume of documents (Id will be auto-incremented)
                customers.InsertBulk(distinctItems);
                // Index document using a document property
                customers.EnsureIndex(x => x.Documento);
            }
        }
    }
}