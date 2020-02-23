import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-simple',
  templateUrl: './simple.component.html',
  styleUrls: ['./simple.component.css']
})
export class SimpleComponent implements OnInit {

  searchForm = new FormGroup({
    field: new FormControl(""),
    value: new FormControl("")
  })

  fieldId: any;
  sciencePaperList: any = [];
  searching: boolean = false;
  emptyList: boolean = false;

  constructor(private searchService: SearchService) { }

  ngOnInit() {
  }

  onSearch(){
    this.emptyList = false;
    this.sciencePaperList = [];
    this.searching = true;

    setTimeout(()=>{

      var simpleQuery = {
        field: this.fieldId,
        value: this.searchForm.value.value,
      }
      
      this.searchService.simpleQuery(simpleQuery).subscribe(
        (response) => {
          this.searching = false;
          this.sciencePaperList = response;
          if(this.sciencePaperList.length == 0){
            this.emptyList = true;
          }
        },
        (error) => { 
          alert(error.message);
          this.searching = false;
        }
      )

    },800);
  }

  getId(event){
    if(event == "Naziv časopisa"){
      this.fieldId = "magazineName";
    }else if(event == "Naslov rada"){
      this.fieldId = "title";
    }else if(event == "Ime i prezime autora"){
      this.fieldId = "author";
    }else if(event == "Ključni pojam"){
      this.fieldId = "keyTerms";
    }else if(event == "Sadržaj PDF-a"){
      this.fieldId = "text";
    }else{
      this.fieldId = "scienceField";
    }
  }

}
