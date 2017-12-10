using System.Web.Http;
using Api.Models;
using LiteDB;
using System.Web;
using Api.Helper;
using System.IO;
using System.Net.Http;
using System.Net;
using System.Net.Http.Headers;
using System.Drawing;
using System.Drawing.Imaging;
using System;
using log4net;

namespace ProductsApp.Controllers
{
    [AllowAnonymous]
    public class AfiliadoController : ApiController
    {
        //Declare an instance for log4net
        private static readonly ILog Log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        [HttpGet, ActionName("get-all")]
        public IHttpActionResult ObtenerAfiliados()
        {
            try
            {
                LiteCollection<AfiliadoModel> customers;
                var databasePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/{0}", ConfigurationHelper.DatabaseFile));
                // Open database (or create if not exits)
                using (var db = new LiteDatabase(databasePath))
                {
                    // Get customer collection
                    customers = db.GetCollection<AfiliadoModel>("customers");
                }

                // Use Linq to query documents
                var list = customers.FindAll();
                // HTTP 200
                return Ok(list);
            }
            catch (Exception ex)
            {
                Log.Error("ActionName: get-all", ex);
                return InternalServerError();
            }
        }

        [HttpGet, ActionName("get-by-id")]
        public IHttpActionResult ObtenerAfiliado([FromUri] QueryStringModel parameters)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            try
            {
                AfiliadoModel model;
                var databasePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/{0}", ConfigurationHelper.DatabaseFile));
                // Open database (or create if not exits)
                using (var db = new LiteDatabase(databasePath))
                {
                    // Get customer collection
                    var customers = db.GetCollection<AfiliadoModel>("customers");
                    // Use Linq to query documents
                    model = customers.FindOne(x => x.Documento.Equals(parameters.Documento));
                }

                if (model == null)
                {
                    return NotFound();
                }
                
                model.ImagenPerfilUrl = Url.Link("DefaultApi", new { controller = "Afiliado", action = "get-profile-image", Documento = model.Documento });

                return Ok(model);
            }
            catch (Exception ex)
            {
                Log.Error("ActionName: get-by-id", ex);
                return InternalServerError();
            }
        }

        [HttpGet, ActionName("get-profile-image")]
        public HttpResponseMessage ObtenerImagenPerfil([FromUri] QueryStringModel parameters)
        {
            if (!ModelState.IsValid)
            {
                return new HttpResponseMessage(HttpStatusCode.BadRequest);
            }

            try
            {
                var imagePath = HttpContext.Current.Server.MapPath(string.Format("~/Data/imagenes/{0}.png", parameters.Documento));
                var defaultImagePath = HttpContext.Current.Server.MapPath("~/Data/imagenes/default.png");
                var fileStream = new FileStream(File.Exists(imagePath) ? imagePath : defaultImagePath, System.IO.FileMode.Open);

                var image = Image.FromStream(fileStream);
                var memoryStream = new MemoryStream();
                image.Save(memoryStream, ImageFormat.Jpeg);

                var result = new HttpResponseMessage(HttpStatusCode.OK);
                result.Content = new ByteArrayContent(memoryStream.ToArray());
                result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");

                return result;
            }
            catch (FileNotFoundException ex)
            {
                Log.Warn("File not found", ex);
                return new HttpResponseMessage(HttpStatusCode.NotFound);
            }
            catch (Exception ex)
            {
                Log.Error("ActionName: get-profile-image", ex);
                return new HttpResponseMessage(HttpStatusCode.InternalServerError);
            }
        }
    }
}