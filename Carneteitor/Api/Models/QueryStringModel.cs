using System.ComponentModel.DataAnnotations;

namespace Api.Models
{
    public class QueryStringModel
    {
        [Required]
        [StringLength(8, MinimumLength = 6)]
        [RegularExpression("^[0-9]*$")]
        public string Documento { get; set; }
    }
}