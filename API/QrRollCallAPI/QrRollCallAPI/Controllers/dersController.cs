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
    public class dersController : ControllerBase
    {

        private readonly AppDbContext _context;

        public dersController(AppDbContext context)
        {
            _context = context;
        }


        [HttpGet]
        public IActionResult Get()
        {
            var ders = _context.ders.ToList();
            return Ok(ders);
        }

        


       

    }
}
