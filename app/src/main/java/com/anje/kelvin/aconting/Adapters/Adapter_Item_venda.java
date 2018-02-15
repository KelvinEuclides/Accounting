package com.anje.kelvin.aconting.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.anje.kelvin.aconting.BaseDeDados.Item;

import java.util.List;

/**
 * Created by sala on 15-02-2018.
 */

public class Adapter_Item_venda extends RecyclerView.Adapter<Adapter_Item_venda.ViewHolder> {

    List<Item> items;
    public Adapter_Item_venda() {
    }

    @Override
    public Adapter_Item_venda.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(Adapter_Item_venda.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder {

        public ViewHolder() {
        }
    }
}
