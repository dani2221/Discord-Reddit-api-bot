import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.ANEWARRAY;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import jdk.internal.net.http.common.Log;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;
import sun.jvm.hotspot.debugger.win32.coff.DebugVC50SymbolEnums;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import javax.sql.rowset.JdbcRowSet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static jdk.internal.net.http.HttpRequestImpl.USER_AGENT;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = ""; //place your own token code from discord here

        builder.setToken(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("majka ti"));
        builder.addEventListeners(new Main());
        builder.build();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        String[] Message = msg.split("\\s+");
        if(Message[0].toLowerCase().equals(".reddit")) {
            String subReddit = Message[1];
            String type = Message[2];
            int count = Integer.parseInt(Message[3]);
            String json = null;
            try {
                json = getJSON("https://www.reddit.com/r/"+subReddit+"/"+type+".json?count="+count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Post post = gson.fromJson(json,Post.class);
            int iii=0;
            for (Child p :post.data.children) {
                if(iii<count) {
                    EmbedBuilder bldr = new EmbedBuilder();
                    bldr.setTitle(p.data.title);
                    bldr.setImage(p.data.url);
                    bldr.setDescription(p.data.selftext);
                    bldr.setColor(Color.CYAN);
                    event.getChannel().sendMessage(bldr.build()).queue();
                    iii++;
                }
                else break;
            }
        }
    }
    public String getJSON(String urll) throws IOException {
        String response="";
        URL url = new URL(urll);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(10000);
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent", "\"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Dani/20100316 Firefox/3.6.2");
        int responceCode = connection.getResponseCode();

        if (responceCode == HttpURLConnection.HTTP_OK)
        {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null)
            {
                response += line;
            }
        }
        else
        {
            response = "";
        }
        return response;
    }
}
