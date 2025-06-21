using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class QR
    {
        [Key]
        public int qr_no { get; set; }

        [Required]
        public int ak_konum_x { get; set; }

        [Required]
        public int ak_konum_y { get; set; }

        [Required]
        public int hafta { get; set; }

        [Required]
        public int ders_no { get; set; }

        [Required]
        public int ak_no { get; set; }

        [Required]
        public int bolum_no { get; set; }

        [Required]
        public int tarih { get; set; }

        [Required]
        public int saat { get; set; }


    }
}
