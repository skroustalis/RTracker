package sokrous.rtracker;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sokrous.rtracker.model.Route;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>{
    private List<Route> routes;

    public RouteAdapter(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(routes.get(position));
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView txtRouteUser;
        private TextView txtRouteName;

        private TextView txtRouteSub;



        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtRouteName = itemView.findViewById(R.id.txtRouteName);
            txtRouteUser = itemView.findViewById(R.id.txtRouteUser);
            txtRouteSub = itemView.findViewById(R.id.txtRouteSub);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtRouteSub.getVisibility() == View.GONE) {
                        txtRouteSub.setVisibility(View.VISIBLE);
                    } else {
                        txtRouteSub.setVisibility(View.GONE);
                    }
                }
            });
        }



        public void bind(Route route) {
            txtRouteName.setText(route.getRouteName());
            txtRouteUser.setText(route.getRouteUser());
            txtRouteSub.setText(route.getRouteUser());
        }
    }
}
