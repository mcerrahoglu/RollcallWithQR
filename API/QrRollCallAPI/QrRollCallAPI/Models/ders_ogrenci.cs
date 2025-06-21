using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class ders_ogrenci
    {
        [Key]
        public int ders_ogrenci_id{ get; set; }

        [Required]
        public int ogr_no{ get; set; }

        [Required]
        public int ders_no{ get; set; }



    }
}
