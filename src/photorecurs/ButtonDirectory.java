/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photorecurs;

import java.io.File;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 *
 * @author User
 */
public class ButtonDirectory extends Button {
    
    private File file;

    public ButtonDirectory(File file) {
        this.file = file;
    }

    public ButtonDirectory(File file, String text) {
        super(text);
        this.file = file;
    }

    public ButtonDirectory(File file, String text, Node graphic) {
        super(text, graphic);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
}
