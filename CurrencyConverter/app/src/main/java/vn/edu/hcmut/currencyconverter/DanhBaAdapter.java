package vn.edu.hcmut.currencyconverter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;




public class DanhBaAdapter extends ArrayAdapter<Item> {
    @NonNull Activity context;//màn hình sử dụng layout này
    @LayoutRes int resource;//layout cho từng dòng muốn hiển thị
    @NonNull List<Item> objects;//danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    public DanhBaAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);// đối số 1 là màn hình item(xml) truyền vào

//        TextView txtTen= row.findViewById(R.id.txtTen);
//        EditText txtPhone=row.findViewById(R.id.txtPhone);
//        ImageButton btnCall= row.findViewById(R.id.btnCall);
//        ImageButton btnSms=row.findViewById(R.id.btnSms);
//        ImageButton btnDetail=row.findViewById(R.id.btnDetail);
        //trả về màn hình muốn vẽ

        ImageView imgCountry=row.findViewById(R.id.imgCountry);
        TextView txtCountry=row.findViewById(R.id.txtCountry);
        TextView txtPrice=row.findViewById(R.id.txtPrice);

        final Item item=this.objects.get(position);

        txtCountry.setText(item.getId());
        txtPrice.setText(item.getPrice().toString());
        imgCountry.setImageResource(item.getPathImg());


//        txtTen.setText(danhba.getName());
//        txtPhone.setText(danhba.getPhone());
//
//        btnDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                xuLiXemChiTiet(danhba);
//            }
//        });

        return row;
    }


}
