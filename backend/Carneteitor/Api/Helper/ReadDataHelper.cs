﻿using Api.Models;
using log4net;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Api.Helper
{
    public static class ReadDataHelper
    {
        //Declare an instance for log4net
        private static readonly ILog Log = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        public static List<AfiliadoModel> Read(string path)
        {
            try
            {
                var book = new LinqToExcel.ExcelQueryFactory(path);

                var query = (from row in book.Worksheet(ConfigurationHelper.SheetName)
                             let item = new AfiliadoModel
                             {
                                 Documento = row[ConfigurationHelper.DocumentColumn].Cast<string>(),
                                 Nombre = row[ConfigurationHelper.NameColumn].Cast<string>(),
                                 Apellido = row[ConfigurationHelper.LastnameColumn].Cast<string>(),
                                 Localidad = row[ConfigurationHelper.StateColumn].Cast<string>(),
                                 Provincia = row[ConfigurationHelper.ProvinceColumn].Cast<string>()
                             }
                             select item).ToList();

                return query;
            }
            catch (Exception ex)
            {
                Log.Error("Read xls file", ex);
                throw;
            }
        }
    }
}