/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projekti;

import java.util.ArrayList;
/**
 *
 * @author hanna
 */
public class KomentoriviKayttoliittyma implements KayttoliittymaRajapinta{
    private IOrajapinta io;
    private TiedostonkasittelijaRajapinta tiedostonKasittelija;
    private Bibtallentaja bibtallentaja;
    
    public KomentoriviKayttoliittyma(IOrajapinta io, TiedostonkasittelijaRajapinta tallentaja) { 
        this.io = io;
        this.tiedostonKasittelija = tallentaja;
        bibtallentaja = new Bibtallentaja();
    }
    
    public KomentoriviKayttoliittyma(IOrajapinta io) { 
        this.io = io;
    }

    public void naytaOhjeet() {
        io.tulosta("\"lisaa\" aloittaa uuden viitteen lisäyksen.");
        io.tulosta("\"lopeta\" lopettaa.");
        io.tulosta("\"lista\" listaa kaikki viitteet.");
        io.tulosta("\"bib\" tulostaa kaikki viitteet bibtex-muodossa.");
        io.tulosta("\n");
    }
    
    public void kaynnista(){
        naytaOhjeet();
        while(true){
            String syote = io.lue();
            if(syote == null){
                naytaOhjeet();
                continue;
            }
            if(syote.equalsIgnoreCase("lopeta")){
                break;
            }
            else if(syote.equalsIgnoreCase("lista")){
                listaa();
            }
            else if(syote.equalsIgnoreCase("lisaa")){
                lisaa();
            }
            else if(syote.equalsIgnoreCase("bib")){
                bibTulostus();
            }
            naytaOhjeet();
            
        }
    }


    public Viite annaViite() {
//        String syote;
//        
//        while(true){
//            syote = io.lue();
//            if(syote == null){
//                naytaOhjeet();
//                continue;
//            }
//            if(syote.equalsIgnoreCase("lopeta") || syote.equalsIgnoreCase("lisaa")){
//                break;
//            }
//            if(syote.equalsIgnoreCase("lista")){
//                tallentaja.tulosta();
//            }
//            naytaOhjeet();
//        }
//       
//        if(syote.equalsIgnoreCase("lopeta")){
//            return null;
//        }
//        
//        if(syote.equalsIgnoreCase("lisaa")){
//        }
//        
//        Viite uusi = kysySyotetta();
//        
//        io.tulosta("lisätään syöte järjestelmään.");
//        naytaOhjeet();
//        return uusi;
        return null;
    }
    
    
    private void lisaa(){
        Viite uusi = kysySyotetta();
        
        io.tulosta("lisätään viite järjestelmään.");
        tiedostonKasittelija.tallenna(uusi);
        
        
    }
    
    private void listaa(){        
        ArrayList<Viite> viitteet = tiedostonKasittelija.lueViitteet();
        
        if(viitteet != null) {
            tulostaViitteet(viitteet);
        }
        else {
            io.tulosta("Viitteita ei ole tai tiedosto ei ole olemassa.\n");
        } 
    }
    
    
    private Viite kysySyotetta(){
        String[] kentat = {"author", "title", "year", "publisher", "booktitle", "pages", 
            "journal", "volume", "number",  "address"};
        String syote;
        Viite uusi = new Viite();
        int i = 0;
        
        uusi.lisaaTietoa("millainenViite", viitteenLaatu());
        uusi.lisaaTietoa("label", annaLabel());
        while(i < kentat.length){
            io.tulosta(kentat[i] + ":");
            syote = io.lue();
            if(syote != null && !syote.equals("")){
                uusi.lisaaTietoa(kentat[i], syote);
            }
            i++;
        }
        return uusi;
    }
    
    private String viitteenLaatu(){
        io.tulosta("Millaisen viitteen haluat lisätä?");
        io.tulosta("(anna numero)");
        io.tulosta("1. @inproceedings \n2. @book \n3. @article");
        String vastaus = io.lue();
        while(!vastaus.equals("1") && !vastaus.equals("2") && !vastaus.equals("3")){
            io.tulosta("(anna numero)");
            io.tulosta("1. @inproceedings \n2. @book \n3. @article");
            vastaus = io.lue();
        }
        if(vastaus.equals("1")){
            return "@inproceedings";
        }
        else if (vastaus.equals("2")){
            return "@book";
        }
        return "@article";
    }
    
    private boolean onkoLabelJoKaytossa(String ehdotettuLabel){
        ArrayList<Viite> viitteet = tiedostonKasittelija.lueViitteet();
        
        if(viitteet == null){
            return false;
        }
        
        int i = 0;
        while(i < viitteet.size()){
            if(viitteet.get(i).getLabel().equalsIgnoreCase(ehdotettuLabel)){
                return true;
            }
            i++;
        }
        
        return false;
        
    }

    private String annaLabel() {
        io.tulosta("Label:");
        String syote = io.lue();
        while(onkoLabelJoKaytossa(syote)){
            io.tulosta("Tämä label on jo käytössä!");
            io.tulosta("Valitse toinen label.");
            io.tulosta("Label:");
            syote = io.lue();
        }
        return syote;
    }
    
    private void tulostaViitteet(ArrayList<Viite> viitteet){
        int i = 0;
        int j = 0;
        String[][] tiedot;
        
        while(i < viitteet.size()){
            tiedot = viitteet.get(i).annaTiedot();
            while(j < tiedot.length){
                if(!tiedot[j][1].equals("")){
                    io.tulosta(tiedot[j][0] + " = " + tiedot[j][1]);
                }
                j++;
            }
            io.tulosta("");
            j = 0;
            i++;
        }
    }

    private void bibTulostus() {
        String tiedostonimi = bibTiedostonNimi();
        
        ArrayList<Viite> viitteet = tiedostonKasittelija.lueViitteet();
        int i = 0;
        while(i < viitteet.size()){
            bibtallentaja.tallenna(viitteet.get(i));
            //bibtallentaja.tallenna(viitteet.get(i), tiedostonimi);
            i++;
        }
    }
    private String bibTiedostonNimi(){
        io.tulosta("Millä nimellä haluat tallentaa bibtex-tiedoston?");
        String tiedostonimi = io.lue();
        while(!onkoKelvollinenBibtexnimi(tiedostonimi)){
            io.tulosta("Epäkelpo nimi!");
            io.tulosta("Anna toinen nimi.");
            tiedostonimi = io.lue();
        }
        
        return tiedostonimi;
    }
    
    private boolean onkoKelvollinenBibtexnimi(String ehdotus){
        if(ehdotus == null || ehdotus.charAt(0) == '.') {
            return false;
        }
        
        int i = 0;
        
        while (i < ehdotus.length() && ehdotus.charAt(i) != '.'){
            i++;
        }
        
        if(i == ehdotus.length()){
            return false;
        }
        if(!ehdotus.substring(i+1).equals("bib")){
            return false;
        }
        
        return true;
    }
  
}
