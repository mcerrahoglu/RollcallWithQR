using System.ComponentModel.DataAnnotations;
namespace QrRollCallAPI.Models
{
    public class ogrenci_sifre
    {
        [Key]
 
        public int ogr_no { get; set; }

        

        [Required]

        public string ogr_sifre { get; set; }

       
        
    }
}
