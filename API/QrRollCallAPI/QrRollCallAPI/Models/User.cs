using System.ComponentModel.DataAnnotations;

namespace QrRollCallAPI.Models
{

    public class User
    {
        public int Id { get; set; }

        [Required]
        public string FirstName { get; set; }

        [Required]
        public string LastName { get; set; }

        
    }
}
