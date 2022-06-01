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

  public artworkNameParser(oldName: string) {
    let result = oldName;
    if (result.includes('.')) {
      result = oldName.substring(0, oldName.lastIndexOf('.'));
    }
    if (result.includes('_')) {
      result = result.substring(result.lastIndexOf('_') + 1, result.length);
    }
    return result;
  }

  base64ToBinaryArray(base64: string) {

    const binary = window.atob(base64);
    const length = binary.length;
    const bytes = new Uint8Array(length);
    for (let i = 0; i < length; i++) {
      bytes[i] = binary.charCodeAt(i);
    }
    return bytes.buffer;
  }
  public shuffleArray(array: any[]): any[] {
    let currentIndex = array.length;
    let randomIndex;

    while (currentIndex >= 0) {
      randomIndex = Math.floor(Math.random() * currentIndex);
      currentIndex--;

      [array[currentIndex], array[randomIndex]] = [array[randomIndex], array[currentIndex]];
    }

    return array;

  }
}
