import { Component, OnInit } from '@angular/core';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-boolean',
  templateUrl: './boolean.component.html',
  styleUrls: ['./boolean.component.css']
})
export class BooleanComponent implements OnInit {

  operation: any;
  sciencePaperList: any = [];
  searching: boolean = false;
  emptyList: boolean = false;
  constructor(private searchService: SearchService) { }

  ngOnInit() {
  }


  onSubmit(value, form){

    if(!form.form.valid){
      alert("Uslovi ispunjenja se mora postaviti.");
      return;
    }

    this.emptyList = false;
    this.sciencePaperList = [];
    this.searching = true;
    var dto = new Array();
    setTimeout(()=>{

      dto = this.getFormFields(value);
      // dodati izbor operacije
      var booleanQuery =  {
        simpleQueryDTOList: dto,
        operation: this.operation
      }
      this.searchService.boolQuery(booleanQuery).subscribe(
        (response) => {
          this.searching = false;
          this.sciencePaperList = response;
          if(this.sciencePaperList.length == 0){
            this.emptyList = true;
          }
        },
        (error) => {
           alert(error.message)
           this.searching = false;
        }
      )
  
    }, 800)
    
  }

  getFormFields(value){
    var list = new Array();
    for(var property in value){
      if(value[property] != ""){
        
        if(property == "operation"){
          if(value[property] == "Da"){
            this.operation = "AND";
          }else{
            this.operation = "OR";
          }
           
        }else{
          list.push({field: property, value: value[property]});
        }
      }
    }
    return list;
  }
}
