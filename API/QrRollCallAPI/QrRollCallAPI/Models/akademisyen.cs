using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class akademisyen
    {
        [Key]
        public int ak_no { get; set; }

        [Required]
        public int ak_tc { get; set; }

        [Required]
        public string ak_ad { get; set; }

        [Required]
        public string ak_soyad { get; set; }

        [Required]
        public string ak_sifre { get; set; }
    }
}
