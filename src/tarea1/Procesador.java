/**
* Paquete Tarea1.
*/
package tarea1;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Clase que contiene los métodos que aplican los filtros:
 * escala de grises, mosaico, alto contraste, brillo y RGB.
 * @author Emmanuel Cruz Hernández. 314272588.
 * @version Tarea1 0.1
 */
public class Procesador {
     
    //Imagen actual que se ha cargado
    public BufferedImage imageActual=null;
    private File imagenSeleccionada;

    /** Método que permite abrir una imagen.
    * @return la imagen cargada de alguna carpeta.
    */
    public BufferedImage abrirImagen(){
        BufferedImage nueva=null;
        JFileChooser selector=new JFileChooser();
        selector.setDialogTitle("Selecciona una imagen");
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("JPG & GIF & BMP","jpg", "gif","bmp");
        selector.setFileFilter(filtro);
        int bandera=selector.showOpenDialog(null);
        // Verificamos que se pulse una imagen.
        if(bandera==JFileChooser.APPROVE_OPTION){
            try {
                // Guardamos la imagen elegida como atributo del objeto.
                imagenSeleccionada=selector.getSelectedFile();
                nueva = ImageIO.read(imagenSeleccionada);
            } catch (IOException e) {}
        }
        // Asignamos la imagen como variable global para acceder a los filtros.
        imageActual=nueva;
        //Retornamos el valor
        return nueva;
    }
     
    /**
     * Verifica si ya se cargó una imagen al procesador de imágenes.
     * @return true si hay imagen cargada, false en otro caso.
     */
    public boolean tieneImagen(){
        return !(imageActual==null);
    }
    
    /**
     * Genera una imagen en escala de Grises.
     * @return la imagen en escala de grises.
     */
    public BufferedImage escalaGrises1(){
        // Regreso la imagen original.
        returnOriginal();
        int mediaPixel;
        Color colorAux,colorSRGB;
        //Recorremos la imagen píxel a píxel
        for( int i = 0; i < this.imageActual.getWidth(); i++ ){
            for( int j = 0; j < this.imageActual.getHeight(); j++ ){
                //Almacenamos el color del píxel
                colorAux=new Color(this.imageActual.getRGB(i, j));
                //Calculamos la media de los tres canales (rojo, verde, azul)
                mediaPixel=(int)((colorAux.getRed()+colorAux.getGreen()+colorAux.getBlue())/3);
                //Cambiamos a formato sRGB
                colorSRGB=new Color(mediaPixel,mediaPixel,mediaPixel);
                //Asignamos el nuevo valor al BufferedImage
                imageActual.setRGB(i, j,colorSRGB.getRGB());
            }
        }
        //Retornamos la imagen
        return imageActual;
    }

    /**
     * Regresa la escala de grises multiplicando el R, G, B por un escalar.
     * @return la imagen en escala de grises.
     */
    public BufferedImage escalaGrises2(){
        returnOriginal();
        for (int i = 0; i < imageActual.getWidth(); i++) {
            for (int j = 0; j < imageActual.getHeight(); j++){
                Color c= new Color(imageActual.getRGB(i, j));
                int aplicado =(int)((c.getRed()*0.29)+(c.getGreen()*0.59)+(c.getBlue()*0.12));
                imageActual.setRGB(i, j, new Color(aplicado,aplicado,aplicado).getRGB());
            }
        }
        return imageActual;
    }
    
    /**
     * Regresa una imagen en escala de grises usando R, G o B como parámetros del color.
     * @param option, la optión  para elegir el color sobre el cual aplicar el tono gris.
     * 0 para Red.
     * 1 para Green.
     * 2 para Blue.
     * @return la imagen en todos de gris.
     */
     public BufferedImage escalaGrises3(int option){
        returnOriginal();
        for (int i = 0; i < imageActual.getWidth(); i++){
            for (int j = 0; j < imageActual.getHeight(); j++){
                Color c = new Color(imageActual.getRGB(i,j));
                int colorRGB=0;
                switch(option){
                    case 0:
                        colorRGB=c.getRed();
                        break;
                    case 1:
                        colorRGB=c.getGreen();
                        break;
                    case 2:
                        colorRGB=c.getBlue();
                        break;
                }
                imageActual.setRGB(i, j, new Color(colorRGB, colorRGB, colorRGB).getRGB());
            }
        }
        return imageActual;
    }
    
    /**
    * Método que regresa la imagen actual a su estado orginal.
    */
    public void returnOriginal(){
        if (imageActual!=null) {
            try {
                imageActual=ImageIO.read(imagenSeleccionada);
            } catch (IOException ex) {
                Logger.getLogger(Procesador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** Filtro de una imagen que aplica un mosaico de pixeles.
     * @param tam Es la cantidad de pixeles por lado de cada mosaico.
     * @return Devuelve el buffer con los pixeles en mosaicos.
     */
    public BufferedImage mosaico(int tam){
        returnOriginal();
        if(tam<=0)
            return imageActual;
        //Recorre las imagenes y obtiene el color de la imagen original y la almacena en el destino.
        int w = imageActual.getWidth(), h = imageActual.getHeight(), 
                x = 0, y = 0, r = 0, g = 0, b = 0, m = 0, n = 0;
        while (x < w){
            m = ((x + tam) < w)? tam : w - x;
            while (y < h){
                n = ((y + tam) < h)? tam : h - y;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++){
                        Color c = new Color(imageActual.getRGB(i + x, j + y));
                        r += c.getRed(); g += c.getGreen(); b += c.getBlue();
                    }
                } r /= (m * n); g /= (m * n); b /= (m * n);
                //Se aplica el color promedio del mosaico.
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) 
                        imageActual.setRGB(i + x, j + y, new Color(r, g, b).getRGB());
                } y += n; r = 0; g = 0; b = 0;
            } x += m; y = 0;
        } return imageActual;
    }


    /** Filtro de una imagen en alto contraste.
     * @return Devuelve el buffer con los pixeles de la imagen en alto contraste.
     */
    public BufferedImage altoContraste(){
        returnOriginal();
        //Recorre las imagenes y obtiene el color de la imagen original y la almacena en el destino
        for (int x = 0; x < imageActual.getWidth(); x++){
            for (int y = 0; y < imageActual.getHeight(); y++){
                //Obtiene el color
                Color c = new Color(imageActual.getRGB(x, y));
                //Verifica el modo y tono de gris del pixel para asignar su nuevo color.
                int n = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                if(n < 128) n = 255;
                else n = 0;
                //Almacena el color en la imagen destino
                imageActual.setRGB(x, y, new Color(n, n, n).getRGB());
            }
        } return imageActual;
    }

    /**
     * Aplica el filtro de brillo a una imagen.
     * @param factor la cantidad de brillo a agregar o disminuir.
     * @return la imagen con el filtro aplicado.
     */
    public BufferedImage brillo(int factor){
        returnOriginal();//chose it according to your need(keep it less than 100) 
        for (int x = 0; x < imageActual.getWidth() ; x++) { 
            for (int y = 0; y < imageActual.getHeight() ; y++) { 
                Color c=new Color(imageActual.getRGB(x,y));
                //adding factor to rgb values 
                int r=c.getRed()+factor;
                int b=c.getBlue()+factor;
                int g=c.getGreen()+factor;
                if (r >= 256)
                    r = 255;
                if (r < 0) 
                    r = 0;
                if (g >= 256) 
                    g = 255;
                if (g < 0)
                    g = 0;
                if (b >= 256)
                    b = 255;
                if (b < 0) 
                    b = 0;
                imageActual.setRGB(x, y,new Color(r,g,b).getRGB());
            }
        }
        return imageActual;
    }

    /**
     * Aplica el filtro RGB. Cierto escalar para aumentar o disminuir los colores
     * rojo, verde y azul en una imagen.
     * @param rMas escalar para sumar al rojo.
     * @param gMas escalar para sumar al verde.
     * @param bMas escalar para sumar al azul.
     * @return la imagen con el filtro aplicado.
     */
    public BufferedImage RGBFilter(int rMas, int gMas, int bMas){
        returnOriginal(); 
        for (int x = 0; x < imageActual.getWidth() ; x++) { 
            for (int y = 0; y < imageActual.getHeight() ; y++) { 
                Color c=new Color(imageActual.getRGB(x,y));
                //adding factor to rgb values 
                int r=c.getRed()+rMas;
                int b=c.getBlue()+bMas;
                int g=c.getGreen() + gMas;
                if (r >= 256)
                    r = 255;
                if (r < 0) 
                    r = 0;
                if (g >= 256) 
                    g = 255;
                if (g < 0)
                    g = 0;
                if (b >= 256)
                    b = 255;
                if (b < 0) 
                    b = 0;
                imageActual.setRGB(x, y,new Color(r,g,b).getRGB());
            }
        }
        return imageActual;
    }
}
