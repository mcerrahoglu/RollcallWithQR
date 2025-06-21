using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using QrRollCallAPI.Data;
using QrRollCallAPI.Models;
using System;
using System.Linq;

namespace QrRollCallAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ogrenci_sifreController : ControllerBase
    {
        private readonly AppDbContext _context;

        public ogrenci_sifreController(AppDbContext context)
        {
            _context = context;
        }

        [HttpPut("{ogr_no}/sifre")]
        public IActionResult Put(int ogr_no, [FromBody] ogrenci_sifre updatedOgrenci)
        {
            if (updatedOgrenci == null)
            {
                return BadRequest("Güncellenmiş öğrenci bilgileri boş olamaz.");
            }

            var existingOgrenci = _context.ogrenci.FirstOrDefault(o => o.ogr_no == ogr_no);

            if (existingOgrenci == null)
            {
                return NotFound(); // Öğrenci bulunamadıysa hata döndür
            }

            // Kontrol etmek istediğiniz özellikleri güncelle
            existingOgrenci.ogr_sifre = updatedOgrenci.ogr_sifre;
            

            try
            {
                _context.SaveChanges(); // Değişikliği kaydet
                return NoContent(); // Başarılı yanıt, içerik değiştirilmedi
            }
            catch (Exception ex)
            {
                // Hata durumunda uygun işlemler burada yapılabilir
                // Örneğin: Loglama, hata mesajı döndürme gibi
                return StatusCode(StatusCodes.Status500InternalServerError, "Veritabanına erişirken bir hata oluştu.");
            }
        }
    }
}
