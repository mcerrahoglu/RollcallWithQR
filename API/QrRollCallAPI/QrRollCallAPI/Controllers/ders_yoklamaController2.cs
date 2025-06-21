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
    public class ders_yoklama2Controller : ControllerBase
    {

        private readonly AppDbContext _context;

        public ders_yoklama2Controller(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Get()
        {
            var ders_yoklama2 = _context.ders_yoklama2.ToList();
            return Ok(ders_yoklama2);
        }


        





    }
}
