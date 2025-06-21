using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class ders
    {
        [Key]
        public int ders_no { get; set; }

        [Required]
        public string ders_ad { get; set; }

        [Required]
        public int ak_no { get; set; }

        [Required]
        public int bolum_no { get; set; }

     
    }
}
