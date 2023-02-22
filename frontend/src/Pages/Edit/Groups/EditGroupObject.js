import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Button, Divider, Grid, Icon, Input, Message, Segment, Select, Table } from 'semantic-ui-react';
import { YEAR_OPTIONS } from '../../../Components/const';
import { EditMenu } from '../../../Components/EditMenu';
import MainMenu from '../../../Components/MainMenu';



const JSON_HEADERS = {
    'Content-Type': 'application/json'
};


export function EditGroupObject() {


    const params = useParams();

    const [active, setActive] = useState(true)

    const [programs, setPrograms] = useState([])



    const shiftOptions = [
        { key: 'r', value: 'morning', text: 'Rytas' },
        { key: 'v', value: 'evening', text: 'Vakaras' },

    ]

    const [error, setError] = useState();


    const [groups, setGroups] = useState({
        name: '',
        studentAmount: '',
        schoolYear: '',
        program: '',
        shift: '',
        modifiedDate: '',
    });

    const [programId, setProgramId] = useState()


    useEffect(() => {
        fetch('/api/v1/groups/' + params.id)
            .then(response => response.json())
            .then(setGroups);
    }, [active, params]);




    const applyResult = () => {

        setActive(true)

    }

    const updateGroups = () => {
        fetch('/api/v1/groups/' + params.id + '?programId=' + programId, {
            method: 'PATCH',
            headers: JSON_HEADERS,
            body: JSON.stringify(groups,)
        }).then(result => {
            if (!result.ok) {
                setError('Update failed. Please fill all fields');
            } else {
                setError()
            }
        }).then(applyResult)
    };

    const updateProperty = (property, event) => {
        setGroups({
            ...groups,
            [property]: event.target.value
        });
    };


    const editThis = () => {
        setActive(false);
        setProgramId(groups.program.id);
    }

    useEffect(() => {
        fetch('/api/v1/programs/')
            .then((response) => response.json())
            .then((data) =>
                setPrograms(
                    data.map((x) => {
                        return { key: x.id, text: x.name, value: x.id };
                    })
                )
            );
    }, []);
    return (


        <div>


            <MainMenu />
            <Grid columns={2}>
                <Grid.Column width={2} id='main'>
                    <EditMenu active='groups' />
                </Grid.Column>
                <Grid.Column floated='left' textAlign='left' verticalAlign='top' width={13}>
                    <Segment id='segment' color='teal'>
                        {active && (<div >
                            <Table celled >
                                <Table.Header >
                                    <Table.Row  >
                                        <Table.HeaderCell >Grupės pavadinimas "Teams"</Table.HeaderCell>
                                        <Table.HeaderCell>Mokslo metai</Table.HeaderCell>
                                        <Table.HeaderCell>Paskutinis atnaujinimas</Table.HeaderCell>
                                        <Table.HeaderCell>Veiksmai</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row  >
                                        <Table.Cell width={6} >{groups.name}</Table.Cell>
                                        <Table.Cell >{groups.schoolYear}</Table.Cell>
                                        <Table.Cell width={3} > {groups.modifiedDate}  </Table.Cell>
                                        <Table.Cell collapsing> <Button id='details' className='controls' onClick={editThis}>Redaguoti</Button> </Table.Cell>
                                    </Table.Row>

                                </ Table.Body ></Table>
                            <Divider horizontal hidden>
                            </Divider>
                            <Table celled >
                                <Table.Header >
                                    <Table.Row  >
                                        <Table.HeaderCell>Studentų skaičius</Table.HeaderCell>
                                        <Table.HeaderCell>Programa</Table.HeaderCell>
                                        <Table.HeaderCell>Pamaina</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>

                                <Table.Body>
                                    <Table.Row  >
                                        <Table.Cell width={6} >{groups.studentAmount}</Table.Cell>
                                        <Table.Cell >{groups.program.name} </Table.Cell>
                                        <Table.Cell width={4}>{groups.shift}</Table.Cell>
                                    </Table.Row>
                                </ Table.Body >
                            </Table>
                            <Button icon labelPosition="left" href='#/view/groups'><Icon name="arrow left" />Atgal</Button>
                        </div>
                        )}


                        {!active && (<div >
                            {error && (<Message warning className='error'>{error}</Message>)}

                            <Table celled >
                                <Table.Header >
                                    <Table.Row  >
                                        <Table.HeaderCell >Grupės pavadinimas "Teams"</Table.HeaderCell>
                                        <Table.HeaderCell>Mokslo metai</Table.HeaderCell>
                                        <Table.HeaderCell>Paskutinis atnaujinimas</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    <Table.Row  >
                                        <Table.Cell width={6}><Input value={groups.name} onChange={(e) => updateProperty('name', e)} />
                                        </Table.Cell>
                                        <Table.Cell >

                                            <select id='selectYear' value={groups.schoolYear} onChange={(e) => updateProperty('schoolYear', e)} >
                                                {Object.entries(YEAR_OPTIONS)
                                                    .map(([key, value]) => <option value={key}>{value}</option>)
                                                }
                                            </select>
                                        </Table.Cell>
                                        <Table.Cell width={4} > {groups.modifiedDate}  </Table.Cell>
                                    </Table.Row>

                                </ Table.Body ></Table>
                            <Divider horizontal hidden>
                            </Divider>
                            <Table celled >
                                <Table.Header >
                                    <Table.Row  >
                                        <Table.HeaderCell>Studentų skaičius</Table.HeaderCell>
                                        <Table.HeaderCell>Programa</Table.HeaderCell>
                                        <Table.HeaderCell>Pamaina</Table.HeaderCell>
                                    </Table.Row>
                                </Table.Header>

                                <Table.Body>
                                    <Table.Row  >
                                        <Table.Cell width={6}><Input value={groups.studentAmount} onChange={(e) => updateProperty('studentAmount', e)} />
                                        </Table.Cell>
                                        <Table.Cell collapsing >
                                            <Select options={programs} placeholder={groups.program.name} onChange={(e, data) => setProgramId(data.value)} />
                                        </Table.Cell >
                                        <Table.Cell width={4}><Input options={shiftOptions} placeholder={groups.shift} value={groups.shift} onChange={(e) => updateProperty('shift', e)} />
                                        </Table.Cell>
                                    </Table.Row>
                                </ Table.Body >
                            </Table>

                            <Button onClick={() => setActive(true)}>Atšaukti</Button><Button className='controls' id='details' onClick={updateGroups}>Atnaujinti</Button>


                        </div>)}

                    </Segment>
                </Grid.Column>
            </Grid>
        </div >
    )
}
