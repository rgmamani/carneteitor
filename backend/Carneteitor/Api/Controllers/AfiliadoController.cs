using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Http;
using Api.Helper;
using Api.Models;
using LiteDB;

namespace Api.Controllers
{
    [AllowAnonymous]
    public class AfiliadoController : ApiController
    {
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

                if (!File.Exists(imagePath))
                {
                    imagePath = HttpContext.Current.Server.MapPath("~/Data/imagenes/default.png");
                }

                var result = new HttpResponseMessage(HttpStatusCode.OK);

                using (var fileStream = new FileStream(imagePath, System.IO.FileMode.Open))
                {
                    var image = Image.FromStream(fileStream);
                    var memoryStream = new MemoryStream();
                    image.Save(memoryStream, ImageFormat.Jpeg);

                    result.Content = new ByteArrayContent(memoryStream.ToArray());
                    result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
                }

                return result;
            }
            catch (FileNotFoundException ex)
            {
                return new HttpResponseMessage(HttpStatusCode.NotFound);
            }
            catch (Exception ex)
            {
                return new HttpResponseMessage(HttpStatusCode.InternalServerError);
            }
        }
    }
}