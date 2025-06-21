using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using QrRollCallAPI.Data;
using System.Collections.Generic;
using System.Linq;
using QrRollCallAPI.Models;



namespace QrRollCallAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AkademisyenController : ControllerBase
    {

        private readonly AppDbContext _context;

        public AkademisyenController(AppDbContext context)
        {
            _context = context;
        }


        [HttpGet]
        public IActionResult Get()
        {
            var akademisyenler = _context.akademisyen.ToList();
            return Ok(akademisyenler);
        }

        


    }
    
}
