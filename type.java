/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sql;

import java.sql.Timestamp;

/**
 *
 * @author Abd sa
 */
public   class type{
      String location_name ;
                String date;
                Timestamp exact_time ;
                int pk_id;
                public String get_location_name(){
                    return this.location_name;
                }
                public int pk_id(){
                    return this.pk_id;
                }
                public String get_date(){
                   return this.date=date;
                }
                 public Timestamp get_exact_time(){
                    return this.exact_time;
                }
                
} 
    

