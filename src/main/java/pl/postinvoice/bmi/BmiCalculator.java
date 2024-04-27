package pl.postinvoice.bmi;

import org.springframework.stereotype.Component;

@Component
public class BmiCalculator {

    BmiCalculation calculate(Integer weight, Integer heightInCm){

        double heightInMeters = heightInCm / 100.0;
        double bmi = weight / heightInMeters / heightInMeters;

        BmiNote bmiNote;
        if(bmi>25){
            bmiNote = BmiNote.NADWAGA;
        }else if (bmi<25 && bmi>=20 ){
            bmiNote = BmiNote.OK;
        }else {
            bmiNote = BmiNote.NIEDOWAGA;
        }

        return new BmiCalculation(bmi, bmiNote);
    }
}
