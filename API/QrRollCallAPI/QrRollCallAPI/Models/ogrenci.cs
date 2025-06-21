using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class ogrenci
    {
        [Key]

        public int ogr_no { get; set; }

        [Required]

        public int ogr_tc { get; set; }

        [Required]

        public string ogr_ad { get; set; }

        [Required]

        public string ogr_soyad { get; set; }

        [Required]

        public int bolum_no { get; set; }

        [Required]

        public string ogr_eposta { get; set; }

        [Required]

        public string ogr_sifre { get; set; }

       
        
    }
}
