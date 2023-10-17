package com.app.radiocity.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.model.ProductDetailsModel;
import com.app.radiocity.R;
import com.app.radiocity.databinding.ProductListBinding;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.itemviewholder> {
    private Context context;
    ArrayList<ProductDetailsModel> ProductList;
    PopupWindow popupWindow;
    String date;
    onItemselection onItemselection;
    int selectedposition = -1;

    public ItemAdapter(ArrayList<ProductDetailsModel> getDataAdapter, Context context, PopupWindow popupWindow,String date, onItemselection onItemselection)
    {
        this.ProductList = getDataAdapter;
        this.context = context;
        this.popupWindow = popupWindow;
        this.onItemselection = onItemselection;
        this.date = date;
    }

    @NonNull
    @Override
    public itemviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ProductListBinding productListBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_list_sub,parent,false);
        return new itemviewholder(productListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull itemviewholder holder, @SuppressLint("RecyclerView") int position) {
        ProductDetailsModel model = ProductList.get(position);
        holder.bind(model);
        holder.setdata(model,onItemselection,position);
    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    public class itemviewholder extends RecyclerView.ViewHolder {
        private ProductListBinding productListBinding;
        public itemviewholder(@NonNull ProductListBinding productListBinding) {
            super(productListBinding.getRoot());
            this.productListBinding = productListBinding;
        }
        public void bind(ProductDetailsModel modal) {
            productListBinding.setProduct(modal);
            productListBinding.executePendingBindings();
        }

        public void setdata(ProductDetailsModel model, ItemAdapter.onItemselection onItemselection, int position) {

            productListBinding.tvSubitemname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemselection.onSelected(productListBinding.tvSubitemname.getText().toString());
                    popupWindow.dismiss();
                }
            });
        }
    }

    public interface onItemselection {
        void onSelected(String item);
    }

}
