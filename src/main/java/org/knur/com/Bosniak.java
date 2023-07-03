package org.knur.com;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bosniak {
    public static org.bukkit.Location minekraftLoadSavedPosition(String mcName)
    {
        try
        {
            Connection c = DB.getInstance().getConnection();
            PreparedStatement s = c.prepareStatement("SELECT * FROM public.user_login WHERE mc_name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            s.setString(1, mcName);
            ResultSet r = s.executeQuery();
            if(r.first())
            {
                float knurX = r.getFloat("x");
                float knurY = r.getFloat("y");
                float knurZ = r.getFloat("z");
                String knurWorld = r.getString("world");

                return new org.bukkit.Location(Bukkit.getWorld(knurWorld), knurX, knurY, knurZ);
            }
            c.close();

            return null;
        }
       catch(SQLException e) {
            System.out.println(e.getMessage());
            return null;
       }
    }

    public static void minekraftSaveLocation(String mcName, org.bukkit.Location location)
    {
        try
        {
            Connection c = DB.getInstance().getConnection();
            PreparedStatement s = c.prepareStatement("INSERT INTO public.user_login(mc_name, x, y, z, world) VALUES (?, ?, ?, ?, ?) ON CONFLICT (mc_name) DO UPDATE SET x = ?, y = ?, z = ?, world = ?");
            s.setString(1, mcName);

            s.setDouble(2, location.getX());
            s.setDouble(3, location.getY());
            s.setDouble(4, location.getZ());
            s.setString(5, location.getWorld().getName());


            s.setDouble(6, location.getX());
            s.setDouble(7, location.getY());
            s.setDouble(8, location.getZ());
            s.setString(9, location.getWorld().getName());

            s.execute();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
