import React, { useEffect, useState } from 'react'
import { Button, Divider, ButtonGroup, Confirm, Icon, Input, Table } from 'semantic-ui-react'
import { CreateRoom } from '../../Create/CreateRoom';
import { EditRoom } from './EditRoom';
import './ViewGroups.css'; 



const JSON_HEADERS = {
    'Content-Type': 'application/json'
};


export function ViewRooms() {
    const [active, setActive] = useState('')

    const [create, setCreate] = useState('')

    const [rooms, setRooms] = useState([]);
//
    const [activePage, setActivePage] = useState(0);
    const [nameText, setNameText] = useState("");
    const [buildingText, setBuildingText] = useState("");
    const [pagecount, setPageCount] = useState()

    const [roomsforPaging, setRoomsForPaging] = useState([]);

    

    const fetchRooms = async () => {
        fetch(`/api/v1/rooms/page?page=` + activePage)
            .then(response => response.json())
            .then(jsonResponse => setRooms(jsonResponse));
    };
    
    const fetchFilterRooms = async () => {
               fetch(`/api/v1/rooms/page/name-filter/${nameText}?page=` + activePage)
                 .then((response) => response.json())
                 .then((jsonRespone) => setRooms(jsonRespone));
             };
    
            
    const removeRoom = (id) => {
        fetch('/api/v1/rooms/' + id, {
            method: 'DELETE',
            headers: JSON_HEADERS
        }).then(fetchRooms).then(setOpen(false));
    }

    const fetchBuildingRooms = async () => {
        fetch(`/api/v1/rooms/page/building-filter/${buildingText}?page=` + activePage)
          .then((response) => response.json())
          .then((jsonRespone) => setRooms(jsonRespone));
      };

      const fetchSingleRooms = async () => {
        fetch('/api/v1/rooms/')
            .then(response => response.json())
            .then(jsonResponse => setRoomsForPaging(jsonResponse))
            .then(setPageCount(Math.ceil(roomsforPaging.length / 10)))
    };


    useEffect(() => {

         if (nameText.length === 0 && buildingText.length === 0){
            fetchRooms();
         } else {
            nameText.length > 0 ? fetchFilterRooms() : fetchBuildingRooms();
            nameText.length > 0 ? setBuildingText('') : setNameText('');
            buildingText.length > 0 ? setNameText('') : setBuildingText('');

         }
        //  else if (buildingText.length > 0){
        //     setNameText('')
        //     fetchBuildingRooms();  
        //  } 
        //  if(nameText.length > 0){
        //         setBuildingText('')
        //         fetchFilterRooms();
        //     }
         
        //  else if (nameText.length > 0 ){
        //     setBuildingText('')
        //     fetchFilterRooms();
        //  } else if(nameText.length > 0 && buildingText > 0){
        //     setNameText('')
        //     fetchBuildingRooms();
        //  }
          //nameText.length > 0 ? fetchFilterRooms() : fetchRooms();
        //  buildingText.length > 0 ? fetchBuildingRooms() : fetchRooms();

             }, [activePage, nameText, buildingText]);

             useEffect(() => {
                if (pagecount !== null) {
                    fetchSingleRooms();
                }
            }, [rooms])


             const [open, setOpen] = useState(false)

            

    return (


        <div>
            {create && (<div><CreateRoom /></div>)}
                
            {active && (<div className='edit'><EditRoom id={active} /></div>)}
                
            {!active && !create && (

                <div id='rooms'>
                    <Input className='controls1' value={nameText} onChange={(e) => setNameText(e.target.value)} placeholder='Filtruoti pagal klasę' />
                    <Input className='controls1' value={buildingText} onChange={(e) => setBuildingText(e.target.value)} placeholder="Filtruoti pagal pastatą"/>

                    

                    <Button icon labelPosition='left' primary className='controls' onClick={() => setCreate('new')}><Icon name='database' />Kurti naują klasę</Button>
                    <Divider horizontal hidden></Divider>
                    <Table selectable >
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell>Klasės pavadinimas</Table.HeaderCell>
                                <Table.HeaderCell>Pastatas</Table.HeaderCell>
                                <Table.HeaderCell>Aprašymas</Table.HeaderCell>
                                <Table.HeaderCell>Veiksmai</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>

                        <Table.Body>
                            {rooms.map(room => (

                                <Table.Row key={room.id}>
                                    <Table.Cell>{room.name}</Table.Cell>
                                    <Table.Cell>{room.building}</Table.Cell>
                                    <Table.Cell>{room.description}</Table.Cell>

                                    <Table.Cell collapsing>
                                        <Button basic primary compact icon='eye' title='Peržiūrėti' onClick={() => setActive(room.id)}></Button>
                                        <Button basic color='black' compact icon='trash alternate' onClick={() => setOpen(room.id)}></Button>
                                        <Confirm
                                            open={open}
                                            header='Dėmesio!'
                                            content='Ar tikrai norite ištrinti?'
                                            cancelButton='Grįžti atgal'
                                            confirmButton="Ištrinti"
                                            onCancel={() => setOpen(false)}
                                            onConfirm={() => removeRoom(open)}
                                            size='small'
                                        />

                                    </Table.Cell>
                                </Table.Row>
                            ))}
                        </Table.Body>
                    </Table>

                    <Divider hidden></Divider>

                    <ButtonGroup basic compact>
                        <Button onClick={() => setActivePage(activePage <= 0 ? activePage : activePage - 1)} icon><Icon name="arrow left" />  </Button>
                        {[...Array(pagecount)].map((e, i) => {
                            return <Button key={i} active={activePage === i ? true : false} onClick={() => setActivePage(i) }>{i + 1}</Button>
                        })}
                        <Button onClick={() => setActivePage(activePage >= pagecount - 1 ? activePage : activePage + 1)} icon><Icon name="arrow right" />  </Button>
                    </ButtonGroup>
                </div>
            )}
        </div>
    )
}