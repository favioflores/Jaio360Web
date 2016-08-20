package com.jaio360.view;

import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class PreferenciasView implements Serializable {

	//private String theme = "aristo"; //default
        private String theme = "aristo"; //default
        

	public String getTheme() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.containsKey("theme")) {
			theme = params.get("theme");
		}
		
		return theme;
	}
        
        public void changeTheme() {
            
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
                if(params.containsKey("globaltheme")) {
                        theme = params.get("globaltheme");
                }
        }

	public void setTheme(String theme) {
		this.theme = theme;
	}
        
        /*
        themes = new ArrayList<Theme>();
        themes.add(new Theme(0, "Afterdark", "afterdark"));
        themes.add(new Theme(1, "Afternoon", "afternoon"));
        themes.add(new Theme(2, "Afterwork", "afterwork"));
        themes.add(new Theme(3, "Aristo", "aristo"));
        themes.add(new Theme(4, "Black-Tie", "black-tie"));
        themes.add(new Theme(5, "Blitzer", "blitzer"));
        themes.add(new Theme(6, "Bluesky", "bluesky"));
        themes.add(new Theme(7, "Bootstrap", "bootstrap"));
        themes.add(new Theme(8, "Casablanca", "casablanca"));
        themes.add(new Theme(9, "Cupertino", "cupertino"));
        themes.add(new Theme(10, "Cruze", "cruze"));
        themes.add(new Theme(11, "Dark-Hive", "dark-hive"));
        themes.add(new Theme(12, "Delta", "delta"));
        themes.add(new Theme(13, "Dot-Luv", "dot-luv"));
        themes.add(new Theme(14, "Eggplant", "eggplant"));
        themes.add(new Theme(15, "Excite-Bike", "excite-bike"));
        themes.add(new Theme(16, "Flick", "flick"));
        themes.add(new Theme(17, "Glass-X", "glass-x"));
        themes.add(new Theme(18, "Home", "home"));
        themes.add(new Theme(19, "Hot-Sneaks", "hot-sneaks"));
        themes.add(new Theme(20, "Humanity", "humanity"));
        themes.add(new Theme(21, "Le-Frog", "le-frog"));
        themes.add(new Theme(22, "MetroUI", "metroui"));
        themes.add(new Theme(23, "Midnight", "midnight"));
        themes.add(new Theme(24, "Mint-Choc", "mint-choc"));
        themes.add(new Theme(25, "Overcast", "overcast"));
        themes.add(new Theme(26, "Pepper-Grinder", "pepper-grinder"));
        themes.add(new Theme(27, "Redmond", "redmond"));
        themes.add(new Theme(28, "Rocket", "rocket"));
        themes.add(new Theme(29, "Sam", "sam"));
        themes.add(new Theme(30, "Smoothness", "smoothness"));
        themes.add(new Theme(31, "South-Street", "south-street"));
        themes.add(new Theme(32, "Start", "start"));
        themes.add(new Theme(32, "Sunny", "sunny"));
        themes.add(new Theme(33, "Swanky-Purse", "swanky-purse"));
        themes.add(new Theme(34, "Trontastic", "trontastic"));
        themes.add(new Theme(35, "UI-Darkness", "ui-darkness"));
        themes.add(new Theme(36, "UI-Lightness", "ui-lightness"));
        themes.add(new Theme(37, "Vader", "vader"));
        */
}
