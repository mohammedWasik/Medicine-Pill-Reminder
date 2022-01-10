package com.example.medicinepillreminderapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public FirstFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        createNotificationChannel();
    }
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference reference;
    String onlineUserId ="";
    String key ="";
    String name="";
    String dose="";
    String stock="";
    String time="";
    String ssdate="";
    String eedate="";
    Uri img;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView imageView;
    Uri imageUri;
    AlarmManager alarmManager;
    int broadcast =0;
    int getBroadcastFire;
    PendingIntent pendingintent;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = v.findViewById(R.id.recylerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
         mAuth = FirebaseAuth.getInstance();
         mUser = mAuth.getCurrentUser();
         onlineUserId = mUser.getUid();
         reference = FirebaseDatabase.getInstance().getReference().child("task").child(onlineUserId);
         storage = FirebaseStorage.getInstance();
         storageReference = storage.getReference();

        FloatingActionButton addTask = v.findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPill(inflater);
            }
        });
        return v;
    }

    public void addPill(@NonNull LayoutInflater inflater) {
        AlertDialog.Builder myDialogue = new AlertDialog.Builder(getContext(), R.style.Theme_MedicinePillReminderApp);
        View myView = inflater.inflate(R.layout.input_ver2, null);
        myDialogue.setView(myView);
        AlertDialog dialog = myDialogue.create();
        dialog.setCancelable(false);
        dialog.show();
        EditText pillName = myView.findViewById(R.id.insertPillName);
        EditText qty = myView.findViewById(R.id.insertDose);
        EditText stock = myView.findViewById(R.id.insertStock);
        EditText startDate = myView.findViewById(R.id.insertStartDate);
        EditText endDate =myView.findViewById(R.id.insertStartDate);
        TextView timeText = myView.findViewById(R.id.insertTime);
        Button save = myView.findViewById(R.id.saveBtn);
        Button cancel = myView.findViewById(R.id.cancelBtn);
        imageView = myView.findViewById(R.id.imageViewInsert);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(openGalleryIntent,1993);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pillName.getText().toString().trim();
                String dose = qty.getText().toString().trim();
                String stockQuantity = stock.getText().toString().trim();
                String id = reference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                String time = timeText.getText().toString().trim();
                String sdate = startDate.getText().toString().trim();
                String edate = endDate.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    pillName.setError("Name Required");
                    return;
                }
                if(TextUtils.isEmpty(dose)){
                    qty.setError("Dosage Required");
                    return;
                }
                if(TextUtils.isEmpty(stockQuantity)){
                    stock.setError("Stock Required");
                    return;
                }
                if(TextUtils.isEmpty(time)){
                    timeText.setError("time Required");
                    return;
                }

                Model model = new Model(name,dose,stockQuantity,id,date,time,sdate,edate,imageUri.toString(),broadcast);
                reference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"Entry added",Toast.LENGTH_SHORT).show();
                            createAlarm(time,broadcast);
                            broadcast++;
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getContext(),"Entry Not added" + task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1993 && resultCode== Activity.RESULT_OK){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Model> option = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference,Model.class)
                .build();
        FirebaseRecyclerAdapter<Model,myViewHolder> adapter = new FirebaseRecyclerAdapter<Model, myViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Model model) {
                holder.setName(model.getName());
                holder.setDosage(model.getDose());
                holder.setQty(model.getStock());
                holder.setTime(model.getTime());
                String a = model.getImg();
                img=Uri.parse(a);
                holder.setImg(img);

                getBroadcastFire=model.getBroadCast();

                name=model.getName();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        key = getRef(holder.getAdapterPosition()).getKey();
                        name=model.getName();
                        dose=model.getDose();
                        stock=model.getStock();
                        time=model.getTime();
                        ssdate=model.getStartDate();
                        eedate=model.getEndDate();
                        String a = model.getImg();
                        img = Uri.parse(a);
                        updateTask();
                    }
                });
            }
            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
                return new myViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void createAlarm(String alarmTime, int broadcast) {
        String[] timestr= alarmTime.split(":");
        int time [] = new int[timestr.length];
        for(int i=0;i<timestr.length;i++){
            time[i]= Integer.parseInt(timestr[i]);
        }
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(),AlarmReceiver.class);
        intent.putExtra("name",name);
        intent.putExtra("id",Integer.toString(broadcast));
        intent.setAction(Integer.toString(broadcast));
        intent.setAction(name);

        pendingintent = PendingIntent.getBroadcast(getActivity(),broadcast,intent,0);
        Calendar calendarG = Calendar.getInstance();
        calendarG.set(Calendar.HOUR_OF_DAY,time[0]);
        calendarG.set(Calendar.MINUTE,time[1]);
        calendarG.set(Calendar.SECOND,0);
        calendarG.set(Calendar.MILLISECOND,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendarG.getTimeInMillis(),pendingintent);
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name =  "MediReminderChannel";
             String description = "Medicine Pill Reminder";
             int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("medi",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManage = getActivity().getSystemService(NotificationManager.class);
            notificationManage.createNotificationChannel(channel);
         }
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView pillName =mView.findViewById(R.id.pillNameShow);
            pillName.setText("Name: "+name);
        }
        public void setDosage(String dose){
            TextView pillDosage = mView.findViewById(R.id.dosageShow);
            pillDosage.setText("Dose: "+dose);
        }
        public void setQty(String stock){
            TextView pillStock = mView.findViewById(R.id.quantityShow);
            pillStock.setText("Pills Left :"+stock);
        }
        public void setTime(String time){
            TextView pillTime = mView.findViewById(R.id.timeShow);
            pillTime.setText("Time :"+time);
        }
        public void setImg(Uri img){
            ImageView imageView = mView.findViewById(R.id.imageViewRetrieve);
            imageView.setImageURI(img);
        }
    }

    private void cancelAlarm(int getBroadcastFire){

        Intent intent = new Intent(getActivity(),AlarmReceiver.class);

        pendingintent = PendingIntent.getBroadcast(getActivity(),broadcast,intent,0);

        if(alarmManager == null){
            alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingintent);


    }


    private void updateTask(){
        AlertDialog.Builder myDialog= new AlertDialog.Builder(getContext(), R.style.Theme_MedicinePillReminderApp);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.update_data,null);
        myDialog.setView(v);
        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(true);
        EditText uName = v.findViewById(R.id.updatePillName);
        EditText uDose = v.findViewById(R.id.updateDose);
        EditText uStock = v.findViewById(R.id.updateStock);
        EditText uTime = v.findViewById(R.id.updateTime);
        Button cancelbtn = v.findViewById(R.id.cancelBtnUpdate);
        Button updatebtn = v.findViewById(R.id.updateBtn);
        Button deletebtn = v.findViewById(R.id.deleteBtn);
        EditText suDate = v.findViewById(R.id.updateStartDate);
        EditText euDate = v.findViewById(R.id.updateEndDate);
        ImageView uImgView = v.findViewById(R.id.imageViewUpdate);
        uName.setText(name);
        uDose.setText(dose);
        uStock.setText(stock);
        uTime.setText(time);
        suDate.setText(ssdate);
        euDate.setText(eedate);
        uImgView.setImageURI(img);
        dialog.show();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = uName.getText().toString().trim();
                dose = uDose.getText().toString().trim();
                stock = uStock.getText().toString().trim();
                time = uTime.getText().toString().trim();
                ssdate=suDate.getText().toString().trim();
                eedate=euDate.getText().toString().trim();

                String date = DateFormat.getDateInstance().format(new Date());
                Model model = new Model(name,dose,stock,key,date,time,ssdate,eedate,img.toString());
                reference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(),"Updated Successfully", Toast.LENGTH_SHORT).show();
                            cancelAlarm(getBroadcastFire);
                            createAlarm(time,getBroadcastFire);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"pill deleted",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
    }
}