import {Injectable} from '@angular/core';
import {FormGroup} from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class GlobalFunctions {
  public mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({mustMatch: true});
      } else {
        matchingControl.setErrors(null);
      }
      return null;
    };
  }
  public artworkNameParser(oldName: string){
    let result=oldName;
    if(result.includes('.')) {
      result = oldName.substring(0, oldName.lastIndexOf('.'));
    }
    if(result.includes('_')){
      result=result.substring(result.lastIndexOf('_')+1,result.length);
    }
    return result;

  }
}
