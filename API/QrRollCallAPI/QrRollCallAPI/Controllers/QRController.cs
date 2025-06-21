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
    public class QRController : ControllerBase
    {

        private readonly AppDbContext _context;

        public QRController(AppDbContext context)
        {
            _context = context;
        }


        [HttpGet]
        public IActionResult Get()
        {
            var QR = _context.QR.ToList();
            return Ok(QR);
        }

        


       

    }
}
