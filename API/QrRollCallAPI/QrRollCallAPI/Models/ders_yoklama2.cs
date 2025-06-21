using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class ders_yoklama2
    {
        [Key]
        public int yok_id { get; set; }

        [Required]
        public int ders_no { get; set; }

        [Required]
        public int ogr_no { get; set; }

        [Required]
        public int hafta { get; set; }

    }
}
