using Api.Models;
using LiteDB;
using log4net;
using System;
using System.Collections.Generic;
using System.Web;

namespace Api.Helper
{
    public class DatabaseHelper
    {
        private static DatabaseHelper instance = null;
        private static string databasePath;
        //Declare an instance for log4net
        private static readonly ILog Log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        protected DatabaseHelper()
        {
            databasePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/{0}", ConfigurationHelper.DatabaseFile));
        }

        public static DatabaseHelper Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new DatabaseHelper();
                }

                return instance;
            }
        }

        public IEnumerable<AfiliadoModel> GetAll()
        {
            try
            {
                LiteCollection<AfiliadoModel> data;
                // Open database (or create if not exits)
                using (var db = new LiteDatabase(databasePath))
                {
                    // Get customer collection
                    data = db.GetCollection<AfiliadoModel>("customers");
                }

                // Use Linq to query documents
                return data.FindAll();
            }
            catch (Exception ex)
            {
                Log.Error("Read LiteDb file", ex);
                throw;
            }
        }

        public int GetTotalCount()
        {
            try
            {
                LiteCollection<AfiliadoModel> data;
                // Open database (or create if not exits)
                using (var db = new LiteDatabase(databasePath))
                {
                    // Get customer collection
                    data = db.GetCollection<AfiliadoModel>("customers");
                }

                // Use Linq to query documents
                return data.Count();
            }
            catch (Exception ex)
            {
                Log.Error("Read LiteDb file", ex);
                throw;
            }
        }

        public AfiliadoModel GetById(string id)
        {
            try
            {
                AfiliadoModel model;
                LiteCollection<AfiliadoModel> data;
                // Open database (or create if not exits)
                using (var db = new LiteDatabase(databasePath))
                {
                    // Get customer collection
                    data = db.GetCollection<AfiliadoModel>("customers");
                    model = data.FindOne(x => x.Documento.Equals(id));
                }

                // Use Linq to query documents
                return model;
            }
            catch (Exception ex)
            {
                Log.Error("Read LiteDb file", ex);
                throw;
            }
        }
    }
}