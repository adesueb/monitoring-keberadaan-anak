package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.PesanData;


/**
 * Interface Koneksi
 */
public interface Koneksi {

  //
  // Fields
  //

  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  /**
   * @param        pesanData
   */
  public void kirimPesanData( PesanData pesanData );


  /**
   * @return       PesanData
   */
  public PesanData menerimaPesanData(  );


}
