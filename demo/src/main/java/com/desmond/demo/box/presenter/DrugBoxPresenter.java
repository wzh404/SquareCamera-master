package com.desmond.demo.box.presenter;

import com.desmond.demo.box.model.DrugBox;
import com.desmond.demo.box.view.DrugBoxView;

import java.util.List;

/**
 * Created by WIN10 on 2016/5/30.
 */
public class DrugBoxPresenter implements DrugBoxView.CallbackDrugBoxView{
    @Override
    public void fresh() {

    }

    @Override
    public List<DrugBox> getDrugBoxes() {
        return null;
    }
}
