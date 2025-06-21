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
    public class ders_yoklamaController : ControllerBase
    {
        private readonly AppDbContext _context;

        public ders_yoklamaController(AppDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult Get()
        {
            var ders_yoklama = _context.ders_yoklama.ToList();
            return Ok(ders_yoklama);
        }

        [HttpPost]
        public IActionResult Post(ders_yoklama model)
        {
            if (ModelState.IsValid)
            {
                // Veritabanında aynı hafta, ders ve öğrenci numarasına sahip kayıtları kontrol et
                var existingRecord = _context.ders_yoklama.FirstOrDefault(x =>
                    x.ders_no == model.ders_no &&
                    x.ogr_no == model.ogr_no &&
                    x.hafta == model.hafta
                );

                // Eğer aynı kayıt varsa hata döndür
                if (existingRecord != null)
                {
                    return BadRequest("Bu kayıt zaten var!");
                }

                // Doğrulama başarılı ise yoklama kaydını ekle
                _context.ders_yoklama.Add(model);
                _context.SaveChanges();

                return Ok();
            }
            return BadRequest(ModelState);
        }
        [HttpDelete("{ders_no}/{ogr_no}/{hafta}")]
        public IActionResult Delete(int ders_no,int ogr_no, int hafta)
        {
            var yoklama = _context.ders_yoklama
                                    .Where(r => r.ders_no == ders_no && r.ogr_no == ogr_no && r.hafta == hafta)
                                    .ToList();

            if (yoklama == null || yoklama.Count == 0)
            {
                return NotFound("Silinecek randevu bulunamadı.");
            }

            _context.ders_yoklama.RemoveRange(yoklama);
            _context.SaveChanges();

            return NoContent();
        }

    }
}
