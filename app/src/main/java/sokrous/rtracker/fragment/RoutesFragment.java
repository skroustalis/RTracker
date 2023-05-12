package sokrous.rtracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sokrous.rtracker.R;
import sokrous.rtracker.RouteAdapter;
import sokrous.rtracker.model.Route;


public class RoutesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RouteAdapter routeAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_route, container, false);

        recyclerView = view.findViewById(R.id.layout_route);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Title 1", "Description 1"));
        routes.add(new Route("Title 2", "Description 2"));
        routes.add(new Route("Title 3", "Description 3"));
        routeAdapter = new RouteAdapter(routes);
        recyclerView.setAdapter(routeAdapter);


        return view;
    }
}
