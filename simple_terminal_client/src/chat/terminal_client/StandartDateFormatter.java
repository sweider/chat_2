/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.terminal_client;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Предоставляет возможность получения строкового представления времени в стандартном для проекта формате
 * @author alex
 */
public class StandartDateFormatter {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public static String formatDate(Date date){
        return formatter.format(date);
    }
}