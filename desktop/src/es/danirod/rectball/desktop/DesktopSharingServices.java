/*
 * This file is part of Rectball
 * Copyright (C) 2015 Dani Rodríguez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.danirod.rectball.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.SharedLibraryLoader;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author danirod
 */
public class DesktopSharingServices implements es.danirod.rectball.platform.SharingServices {

    @Override
    public void shareScreenshot(Pixmap pixmap) {
        Gdx.app.debug("SharingServices", "Requesting the user for location...");
        FileHandle output = requestSave();
        if (output != null) {
            Gdx.app.debug("SharingServices", "Saving screenshot to " + output.path() + "...");
            PixmapIO.writePNG(output, pixmap);
        }
    }

    @Override
    public void shareGameOverScreenshot(Pixmap pixmap, int score, int time) {
        Gdx.app.debug("SharingServices", "Requested a screenshot with score");
    }

    private FileHandle requestSave() {
        String location = null;

        // Use a JFileChooser on Linux and on Windows. Apparently, Apple's
        // Java integration recommendations suggest using old AWT FileDialog
        // because of the better integration with the rest of the system.
        if (SharedLibraryLoader.isMac) {
            FileDialog dialog = new FileDialog((Frame) null, "Save screenshot to", FileDialog.SAVE);
            dialog.setFile("*.png");
            dialog.setMultipleMode(false);
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                location = dialog.getDirectory() + dialog.getFile();
            }
            dialog.dispose();
        } else {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save screenshot to");
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                location = chooser.getSelectedFile().getAbsolutePath();
            }
        }

        return location != null ? Gdx.files.absolute(location) : null;
    }
}
