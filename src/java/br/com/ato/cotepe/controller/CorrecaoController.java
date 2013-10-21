/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ato.cotepe.controller;

import br.com.ato.cotepe.dao.CaminhosArquivosDao;
import br.com.ato.cotepe.dao.ICaminhosArquivosDao;
import br.com.ato.cotepe.entities.ReducoesZ;
import br.com.ato.cotepe.entities.TipoE14;
import br.com.ato.cotepe.faces.FacesBean;
import br.com.ato.cotepe.util.DateHelper;
import br.com.ato.cotepe.util.FileHelper;
import br.com.ato.cotepe.util.Hash;
import br.com.ato.cotepe.util.StringHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author jacoboliveira
 */
public class CorrecaoController extends FacesBean {

    private UploadedFile file;
    private boolean visivelBotaoDownload;
    private File arquivo;
    private String textoRegistros;

    public String getTextoRegistros() {
        return textoRegistros;
    }

    public void setTextoRegistros(String textoRegistros) {
        this.textoRegistros = textoRegistros;
    }


    public boolean isVisivelBotaoDownload() {
        return visivelBotaoDownload;
    }

    public void setVisivelBotaoDownload(boolean visivelBotaoDownload) {
        this.visivelBotaoDownload = visivelBotaoDownload;
    }
    
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String upload() {
        try {
            if(file==null){                
               throw new Exception("Por favor localize um arquivo!");                    
            }
            String nomeRealArquivo = file.getFileName();
            if (!StringHelper.endsWithIgnoreCase(nomeRealArquivo, ".txt")) {
                throw new Exception("Por favor insira somente arquivos textos(*.txt)!");
            }
            
            arquivo = inputStreamToFile();

            String enconding = "ISO-8859-1";
            List<String> linhas = FileHelper.readLines(arquivo, enconding);

            List<ReducoesZ> reducoesZList = new ArrayList<ReducoesZ>();
            List<TipoE14> e14cooList = new ArrayList<TipoE14>();
            boolean contemErros = false;
            int contRegistros=0;
            int contProdutos=0;
            for (String linha : linhas) {

                if (linha.substring(0, 3).equals("E12")) {
                    ReducoesZ reducoesZ = new ReducoesZ();
                    reducoesZ.setCoo(linha.substring(52, 58));
                    reducoesZ.setDataMovimento(linha.substring(64, 72));
                    reducoesZList.add(reducoesZ);
                }
            }
            for (ReducoesZ reducoesZ : reducoesZList) {
                StringBuilder conteudo = new StringBuilder();
                linhas = FileHelper.readLines(arquivo, enconding);
                for (String linha : linhas) {

                    if (linha.substring(0, 3).equals("E14")) {
                        String dataVerificar = linha.substring(58, 66);
                        String e14Coo = linha.substring(52, 58);
                        if (dataVerificar.equals(reducoesZ.getDataMovimento()) && !reducoesZ.getCoo().equals(e14Coo)) {
                            contemErros = true;
                            contRegistros++;
                            
                            StringBuilder linhaAlt = new StringBuilder(linha);
                            linhaAlt.replace(52, 58, reducoesZ.getCoo());
                            linha = linhaAlt.toString();

                            String ccf = linha.substring(46, 52);
                            String coo = linha.substring(52, 58);
                            TipoE14 tipoE14Comparar = new TipoE14();
                            tipoE14Comparar.setCcf(ccf);
                            tipoE14Comparar.setCoo(coo);
                            if (!isContem(tipoE14Comparar, e14cooList)) {
                                TipoE14 tipoE14 = new TipoE14();
                                tipoE14.setCcf(ccf);
                                tipoE14.setCoo(coo);
                                e14cooList.add(tipoE14);
                            }

                        }
                    }
                    conteudo.append(linha).append(File.separator.equals("\\") ? "\r\n" : "\n");
                }
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(arquivo), enconding));
                writer.write(conteudo.toString());
                writer.flush();
                writer.close();

            }


            for (TipoE14 tipoE14 : e14cooList) {
                StringBuilder conteudo = new StringBuilder();
                linhas = FileHelper.readLines(arquivo, enconding);
                for (String linha : linhas) {
                    if (linha.substring(0, 3).equals("E15")) {

                        String ccf = linha.substring(52, 58);
                        if (ccf.equals(tipoE14.getCcf())) {
                            StringBuilder alterarLinha = new StringBuilder(linha);
                            alterarLinha.replace(46, 52, tipoE14.getCoo());
                            linha = alterarLinha.toString();
                            contProdutos++;
                        }

                    }
                    conteudo.append(linha).append(File.separator.equals("\\") ? "\r\n" : "\n");
                }
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(arquivo), enconding));
                writer.write(conteudo.toString());
                writer.flush();
                writer.close();
            }
            if(contemErros){                               
                textoRegistros= "Foram encontrado(s) "+contRegistros+" registros e "+ contProdutos +" produto(s)" ;
                info("upload realizado com sucesso!");
                visivelBotaoDownload = true;
            }else{
                info("Arquivo não contem erros!");  
                visivelBotaoDownload = false;
                textoRegistros = null;
            }
            
           
        } catch (Exception ex) {
            Logger.getLogger(CorrecaoController.class.getName()).log(Level.SEVERE, null, ex);
            error(ex.getMessage());
            visivelBotaoDownload = false;
        }
        return "home";
    }

    public File inputStreamToFile() throws IOException, Exception {
        ICaminhosArquivosDao caminhosArquivosDao= CaminhosArquivosDao.instance();
        
        String fileName = file.getFileName();
        String url = caminhosArquivosDao.findById(1L).getCaminho();
        String nomeArquivo = StringHelper.substringBeforeLast(fileName, ".");
        String extensao = StringHelper.substringAfterLast(fileName, ".");

        final File arquivoUploaded = new File(url + File.separator + Hash.md5(nomeArquivo + DateHelper.getDataHoje(DateHelper.FORMAT_YYYYMMDD_HHMM)) + "." + extensao);
        arquivoUploaded.createNewFile();
        FileOutputStream out = new FileOutputStream(arquivoUploaded);
        IOUtils.copy(file.getInputstream(), out);
        out.close();
        return arquivoUploaded;
    }
    
    public StreamedContent getFileDownload() throws FileNotFoundException{
        InputStream stream = new FileInputStream(arquivo);  
        StreamedContent contentFile = new DefaultStreamedContent(stream, "text/plain", arquivo.getName());  
        return contentFile;
    }
    
    private static boolean isContem(TipoE14 tipoE14Comparar, List<TipoE14> e14cooList) {
        for (TipoE14 tipoE14 : e14cooList) {
            if (tipoE14.getCcf().equals(tipoE14Comparar.getCcf())) {
                return true;
            }
        }
        return false;
    }
}
