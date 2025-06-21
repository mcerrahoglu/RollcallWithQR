 using Microsoft.EntityFrameworkCore;
using QrRollCallAPI.Models;
using System.Collections.Generic;

namespace QrRollCallAPI.Data
{
    public class AppDbContext : DbContext
    {

        public AppDbContext(DbContextOptions<AppDbContext> options): base(options){ }

        public DbSet<akademisyen> akademisyen { get; set; }
        public DbSet<ogrenci> ogrenci { get; set; }
        public DbSet<ders> ders { get; set; }
        public DbSet<QR> QR { get; set; }
        public DbSet<ders_yoklama> ders_yoklama { get; set; }
        public DbSet<ders_yoklama2> ders_yoklama2 { get; set; }
        public DbSet<ogrenci_sifre> ogrenci_sifre { get; set;  }
        public DbSet<ders_ogrenci> ders_ogrenci { get; set; }



    }
}
