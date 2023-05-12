package sokrous.rtracker.model;

import java.util.List;

public class Route {

    public String routeName;
    public String routeUser;
//    public Enum routeType;
//    public List<Point> routePoints;



    public Route(String routeName, String routeUser ){
        this.routeName =routeName;
        this.routeUser = routeUser;
//        this.routeType = routeType;
//        this.routePoints = routePoints;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteUser() {
        return routeUser;
    }

    public void setRouteUser(String routeUser) {
        this.routeUser = routeUser;
    }

//    public Enum getRouteType() {
//        return routeType;
//    }
//
//    public void setRouteType(Enum routeType) {
//        this.routeType = routeType;
//    }
//
//    public List<Point> getRoutePoints() {
//        return routePoints;
//    }
//
//    public void setRoutePoints(List<Point> routePoints) {
//        this.routePoints = routePoints;
//    }
}
