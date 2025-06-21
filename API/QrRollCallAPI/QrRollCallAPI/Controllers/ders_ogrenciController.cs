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
    public class ders_ogrenciController : ControllerBase
    {

        private readonly AppDbContext _context;

        public ders_ogrenciController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Get()
        {
            var ders_ogrenci = _context.ders_ogrenci.ToList();
            return Ok(ders_ogrenci);
        }


        





    }
}
